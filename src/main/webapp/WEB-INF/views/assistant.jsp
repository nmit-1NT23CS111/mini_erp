<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="AI Assistant" scope="request"/>
<c:set var="activeMenu" value="assistant" scope="request"/>
<%@ include file="layout/header.jsp" %>

<div class="page-header">
    <div class="page-header-left">
        <h1><i class="bi bi-robot me-2 text-primary"></i>AI Business Assistant</h1>
        <p>Ask natural-language questions about inventory, revenue, customers, and recent sales.</p>
    </div>
</div>

<div class="ai-assistant-panel">
    <div class="ai-assistant-header">
        <div>
            <div class="fw-bold">Mini ERP AI</div>
            <small class="text-muted">Powered by Ollama • ${sessionScope.loggedInUser}</small>
        </div>
        <span class="ai-status-badge"><i class="bi bi-circle-fill me-1"></i>Online</span>
    </div>

    <div class="ai-chat-window" id="chatWindow" aria-live="polite">
        <div class="message ai">
            <div class="message-bubble">
                Hello! I can help with inventory, sales revenue, top products, customer performance, and recent business summaries. Ask me anything about the ERP data.
            </div>
        </div>
    </div>

    <div class="ai-loading" id="aiLoading" hidden>
        <span class="spinner"></span>
        <span>Thinking...</span>
    </div>

    <div class="ai-error" id="aiError" hidden></div>

    <form id="assistantForm" class="ai-input-row">
        <input type="text" id="questionInput" class="form-control ai-input" placeholder="Ask about products, sales, inventory, or customers..." maxlength="500" autocomplete="off" />
        <button type="submit" class="btn-primary-erp" id="sendButton">
            <i class="bi bi-send-fill me-1"></i>Send
        </button>
    </form>
</div>

<script>
    const contextPath = '${pageContext.request.contextPath}';
    const chatWindow = document.getElementById('chatWindow');
    const form = document.getElementById('assistantForm');
    const input = document.getElementById('questionInput');
    const loading = document.getElementById('aiLoading');
    const errorBox = document.getElementById('aiError');

    function appendMessage(role, text) {
        const wrapper = document.createElement('div');
        wrapper.className = 'message ' + role;

        const bubble = document.createElement('div');
        bubble.className = 'message-bubble';
        bubble.textContent = text;

        wrapper.appendChild(bubble);
        chatWindow.appendChild(wrapper);
        chatWindow.scrollTop = chatWindow.scrollHeight;
    }

    function showError(message) {
        errorBox.textContent = message;
        errorBox.hidden = false;
    }

    function clearError() {
        errorBox.textContent = '';
        errorBox.hidden = true;
    }

    function setLoading(isLoading) {
        loading.hidden = !isLoading;
        const sendButton = document.getElementById('sendButton');
        sendButton.disabled = isLoading;
        sendButton.innerHTML = isLoading
            ? '<span class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>Sending'
            : '<i class="bi bi-send-fill me-1"></i>Send';
    }

    form.addEventListener('submit', async function (event) {
        event.preventDefault();
        const question = input.value.trim();
        if (!question) {
            showError('Please enter a question before sending.');
            return;
        }

        clearError();
        appendMessage('user', question);
        input.value = '';
        setLoading(true);

        try {
            const response = await fetch(contextPath + '/api/assistant/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({ question: question })
            });

            const payload = await response.json().catch(() => null);
            if (!response.ok || !payload || !payload.success) {
                throw new Error((payload && payload.message) || 'The assistant could not process the request.');
            }

            const answer = payload.data && payload.data.answer ? payload.data.answer : 'No answer was returned.';
            appendMessage('ai', answer);
            if (payload.data && payload.data.source === 'Fallback') {
                showError(answer);
            }
        } catch (error) {
            appendMessage('ai', 'I could not answer that request right now. ' + (error.message || 'Please try again in a moment.'));
            showError(error.message || 'Something went wrong.');
        } finally {
            setLoading(false);
            input.focus();
        }
    });
</script>

<%@ include file="layout/footer.jsp" %>
