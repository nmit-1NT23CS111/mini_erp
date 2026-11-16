package com.minierp.service;

import com.minierp.dao.CustomerDao;
import com.minierp.dao.ProductDao;
import com.minierp.dao.SaleDao;
import com.minierp.dto.AiAssistantResponse;
import com.minierp.model.Product;
import com.minierp.model.Sale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class AiAssistantService {

    private static final Logger logger = LoggerFactory.getLogger(AiAssistantService.class);
    private static final DateTimeFormatter SALE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OllamaConfig ollamaConfig;

    @Autowired
    private OllamaClient ollamaClient;

    public AiAssistantResponse askQuestion(String question) {
        String normalizedQuestion = question == null ? "" : question.trim();
        if (normalizedQuestion.isEmpty()) {
            return new AiAssistantResponse(
                    "",
                    "Please enter a business question so I can check the ERP data.",
                    ollamaConfig.getModel(),
                    "ERP Data"
            );
        }

        if (!isSupportedQuestion(normalizedQuestion)) {
            return new AiAssistantResponse(
                    normalizedQuestion,
                    "I can answer questions about product count, low stock, inventory, total revenue, top selling products, top customers, recent sales, and overall business performance summaries. You can also ask me to introduce myself.",
                    ollamaConfig.getModel(),
                    "ERP Data"
            );
        }

        BusinessContext context = buildBusinessContext();
        if (context.isEmpty() && !isGreetingQuestion(normalizedQuestion)) {
            return new AiAssistantResponse(
                    normalizedQuestion,
                    "I checked the ERP database and it currently has no products, customers, or sales data. Add some records first, then ask me again about inventory, sales, or customer performance.",
                    ollamaConfig.getModel(),
                    "ERP Data"
            );
        }

        String prompt = buildPrompt(normalizedQuestion, context);

        try {
            String answer = ollamaClient.generate(prompt);
            String cleanAnswer = sanitizeAnswer(answer);
            return new AiAssistantResponse(normalizedQuestion, cleanAnswer, ollamaConfig.getModel(), "Ollama");
        } catch (Exception ex) {
            logger.error("AI business assistant request failed. Ollama URL: {} Model: {}",
                    ollamaConfig.getBaseUrl(), ollamaConfig.getModel(), ex);
            return new AiAssistantResponse(
                    normalizedQuestion,
                    getOllamaFailureMessage(ex),
                    ollamaConfig.getModel(),
                    "Fallback"
            );
        }
    }

    private boolean isSupportedQuestion(String question) {
        String lower = question.toLowerCase(Locale.ROOT);
        return lower.contains("product")
                || lower.contains("stock")
                || lower.contains("inventory")
                || lower.contains("sale")
                || lower.contains("sales")
                || lower.contains("revenue")
                || lower.contains("customer")
                || lower.contains("business")
                || lower.contains("summary")
                || lower.contains("performance")
                || lower.contains("recent")
                || lower.contains("top")
                || lower.contains("hello")
                || lower.contains("introduce")
                || lower.contains("who are you");
    }

    private boolean isGreetingQuestion(String question) {
        String lower = question.toLowerCase(Locale.ROOT);
        return lower.contains("hello")
                || lower.contains("introduce")
                || lower.contains("who are you");
    }

    private String getOllamaFailureMessage(Exception exception) {
        if (hasCause(exception, HttpConnectTimeoutException.class) || hasCause(exception, ConnectException.class)) {
            return "Could not connect to Ollama at " + ollamaConfig.getBaseUrl() + ". Check that Ollama is running and that the configured URL is reachable.";
        }
        if (hasCause(exception, HttpTimeoutException.class)) {
            return "Ollama did not finish generating a response within "
                    + ollamaConfig.getTimeout().toSeconds()
                    + " seconds. The model may still be generating; try again or increase ollama.timeout-ms.";
        }
        if (exception instanceof IllegalStateException) {
            return exception.getMessage();
        }
        if (exception instanceof com.fasterxml.jackson.core.JsonProcessingException) {
            return "Ollama returned an invalid JSON response. Check the server log for details.";
        }
        return "Ollama request failed. Check the server log for details.";
    }

    private boolean hasCause(Throwable exception, Class<? extends Throwable> type) {
        for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
            if (type.isInstance(cause)) {
                return true;
            }
        }
        return false;
    }

    private BusinessContext buildBusinessContext() {
        long totalProducts = productDao.count();
        long totalCustomers = customerDao.count();
        long totalSales = saleDao.count();
        BigDecimal totalRevenue = saleDao.totalRevenue();
        long inventoryUnits = productDao.totalInventoryQuantity();

        List<Product> lowStockProducts = productDao.findLowStock(Product.LOW_STOCK_THRESHOLD);
        List<Sale> recentSales = saleDao.findRecent(5);

        List<String> topProductLines = new ArrayList<>();
        List<Map<String, Object>> topProducts = jdbcTemplate.queryForList(
                "SELECT p.name AS product_name, COALESCE(SUM(si.quantity), 0) AS units_sold, " +
                        "COALESCE(SUM(si.subtotal), 0) AS revenue " +
                        "FROM sale_items si JOIN products p ON p.id = si.product_id " +
                        "GROUP BY p.id, p.name ORDER BY units_sold DESC, revenue DESC LIMIT 5"
        );

        for (Map<String, Object> row : topProducts) {
            String productName = String.valueOf(row.getOrDefault("product_name", "Unknown"));
            String unitsSold = formatValue(row.getOrDefault("units_sold", 0));
            String revenue = formatCurrency(row.getOrDefault("revenue", BigDecimal.ZERO));
            topProductLines.add(productName + " (units sold: " + unitsSold + ", revenue: ₹" + revenue + ")");
        }

        List<String> topCustomerLines = new ArrayList<>();
        List<Map<String, Object>> topCustomers = jdbcTemplate.queryForList(
                "SELECT c.name AS customer_name, COALESCE(SUM(s.total_amount), 0) AS total_spend, " +
                        "COUNT(s.id) AS order_count " +
                        "FROM sales s JOIN customers c ON c.id = s.customer_id " +
                        "GROUP BY c.id, c.name ORDER BY total_spend DESC LIMIT 5"
        );

        for (Map<String, Object> row : topCustomers) {
            String customerName = String.valueOf(row.getOrDefault("customer_name", "Unknown"));
            String totalSpend = formatCurrency(row.getOrDefault("total_spend", BigDecimal.ZERO));
            String orderCount = formatValue(row.getOrDefault("order_count", 0));
            topCustomerLines.add(customerName + " (orders: " + orderCount + ", spend: ₹" + totalSpend + ")");
        }

        List<String> lowStockLines = new ArrayList<>();
        for (Product product : lowStockProducts) {
            lowStockLines.add(product.getName() + " (ID " + product.getId() + ", stock " + product.getQuantity() + ")");
        }

        List<String> recentSaleLines = new ArrayList<>();
        for (Sale sale : recentSales) {
            String saleDate = sale.getSaleDate() == null ? "unknown" : sale.getSaleDate().format(SALE_FORMATTER);
            recentSaleLines.add("#" + sale.getId() + " - " + sale.getCustomerName() + " - ₹" + formatCurrency(sale.getTotalAmount()) + " - " + saleDate);
        }

        return new BusinessContext(
                totalProducts,
                totalCustomers,
                totalSales,
                totalRevenue,
                inventoryUnits,
                lowStockLines,
                recentSaleLines,
                topProductLines,
                topCustomerLines
        );
    }

    private String buildPrompt(String question, BusinessContext context) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a concise and professional business assistant for a Mini ERP system. Use only the data below to answer the question. Do not invent numbers or facts. Keep the answer short, readable, and business-focused.\n\n");
        prompt.append("ERP database snapshot:\n");
        prompt.append("- Total products: ").append(context.totalProducts).append("\n");
        prompt.append("- Total customers: ").append(context.totalCustomers).append("\n");
        prompt.append("- Total sales: ").append(context.totalSales).append("\n");
        prompt.append("- Total revenue: ₹").append(formatCurrency(context.totalRevenue)).append("\n");
        prompt.append("- Total inventory units: ").append(context.inventoryUnits).append("\n");
        prompt.append("- Low stock items: ").append(context.lowStockLines.isEmpty() ? "None" : String.join("; ", context.lowStockLines)).append("\n");
        prompt.append("- Top selling products: ").append(context.topProductLines.isEmpty() ? "No sales data" : String.join("; ", context.topProductLines)).append("\n");
        prompt.append("- Top customers: ").append(context.topCustomerLines.isEmpty() ? "No customer data" : String.join("; ", context.topCustomerLines)).append("\n");
        prompt.append("- Recent sales: ").append(context.recentSaleLines.isEmpty() ? "No recent sales" : String.join("; ", context.recentSaleLines)).append("\n\n");
        prompt.append("User question: ").append(question).append("\n\n");
        prompt.append("Respond as if you are helping a business owner. Mention exact values only from the data above and clearly say if no data exists.");
        return prompt.toString();
    }

    private String sanitizeAnswer(String value) {
        if (value == null) {
            return "I could not generate a response from the ERP data.";
        }
        String cleaned = value.trim();
        if (cleaned.startsWith("\n")) {
            cleaned = cleaned.replaceFirst("^\\s+", "");
        }
        return cleaned;
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "0";
        }
        if (value instanceof Number) {
            BigDecimal decimal = new BigDecimal(value.toString());
            return decimal.setScale(0, RoundingMode.HALF_UP).toPlainString();
        }
        return String.valueOf(value);
    }

    private String formatCurrency(Object value) {
        if (value == null) {
            return "0.00";
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).setScale(2, RoundingMode.HALF_UP).toPlainString();
        }
        if (value instanceof Number) {
            return new BigDecimal(value.toString()).setScale(2, RoundingMode.HALF_UP).toPlainString();
        }
        return new BigDecimal(String.valueOf(value)).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static final class BusinessContext {
        private final long totalProducts;
        private final long totalCustomers;
        private final long totalSales;
        private final BigDecimal totalRevenue;
        private final long inventoryUnits;
        private final List<String> lowStockLines;
        private final List<String> recentSaleLines;
        private final List<String> topProductLines;
        private final List<String> topCustomerLines;

        private BusinessContext(long totalProducts,
                               long totalCustomers,
                               long totalSales,
                               BigDecimal totalRevenue,
                               long inventoryUnits,
                               List<String> lowStockLines,
                               List<String> recentSaleLines,
                               List<String> topProductLines,
                               List<String> topCustomerLines) {
            this.totalProducts = totalProducts;
            this.totalCustomers = totalCustomers;
            this.totalSales = totalSales;
            this.totalRevenue = totalRevenue == null ? BigDecimal.ZERO : totalRevenue;
            this.inventoryUnits = inventoryUnits;
            this.lowStockLines = lowStockLines;
            this.recentSaleLines = recentSaleLines;
            this.topProductLines = topProductLines;
            this.topCustomerLines = topCustomerLines;
        }

        private boolean isEmpty() {
            return totalProducts == 0 && totalCustomers == 0 && totalSales == 0 && totalRevenue.compareTo(BigDecimal.ZERO) == 0;
        }
    }
}
