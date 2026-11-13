<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    </div><!-- .page-body -->
</div><!-- .main-content -->

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- Chart.js (loaded globally for dashboard) -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.4/dist/chart.umd.min.js"></script>
<!-- Custom JS -->
<script src="${pageContext.request.contextPath}/resources/js/app.js"></script>
<script>
    // Topbar clock
    function updateClock() {
        const now = new Date();
        document.getElementById('topbarTime').textContent =
            now.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit' });
    }
    updateClock();
    setInterval(updateClock, 60000);
</script>
</body>
</html>
