document.addEventListener('DOMContentLoaded', function() {
    // Form validation for add/edit books
    const forms = document.querySelectorAll('.book-form');

    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            let isValid = true;
            const inputs = form.querySelectorAll('input[required], select[required]');

            inputs.forEach(input => {
                if (!input.value.trim()) {
                    isValid = false;
                    input.style.borderColor = '#e74c3c';

                    // Create error message if not exists
                    if (!input.nextElementSibling || !input.nextElementSibling.classList.contains('error-message')) {
                        const errorMsg = document.createElement('small');
                        errorMsg.className = 'error-message';
                        errorMsg.style.color = '#e74c3c';
                        errorMsg.textContent = 'This field is required';
                        input.parentNode.insertBefore(errorMsg, input.nextSibling);
                    }
                } else {
                    input.style.borderColor = '#ddd';
                    const errorMsg = input.nextElementSibling;
                    if (errorMsg && errorMsg.classList.contains('error-message')) {
                        errorMsg.remove();
                    }
                }
            });

            if (!isValid) {
                e.preventDefault();

                // Scroll to first error
                const firstError = form.querySelector('input[style*="border-color: rgb(231, 76, 60)"]');
                if (firstError) {
                    firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                }
            }
        });
    });

    // Status color coding in tables
    const statusCells = document.querySelectorAll('td span[class^="status-"]');
    statusCells.forEach(cell => {
        if (cell.classList.contains('status-available')) {
            cell.innerHTML = `<i class="fas fa-check-circle"></i> ${cell.textContent}`;
        } else if (cell.classList.contains('status-checkedout')) {
            cell.innerHTML = `<i class="fas fa-times-circle"></i> ${cell.textContent}`;
        }
    });

    // Confirmation for delete actions
    const deleteButtons = document.querySelectorAll('.btn-delete');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('Are you sure you want to delete this book?')) {
                e.preventDefault();
            }
        });
    });

    // Search form enhancement
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        const searchInput = searchForm.querySelector('input[name="query"]');

        searchInput.addEventListener('focus', function() {
            this.placeholder = 'Try "101" or "Harry Potter"';
        });

        searchInput.addEventListener('blur', function() {
            this.placeholder = 'Search by id or title...';
        });
    }
});