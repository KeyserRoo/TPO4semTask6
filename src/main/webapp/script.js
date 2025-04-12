document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('search-form');
    const namesInput = document.querySelector('input[name="name"]');
    const directorsInput = document.querySelector('input[name="director"]');
    const genresSelect = document.querySelector('select[name="genre"]');
    const scoreOrderSelect = document.querySelector('select[name="scoreOrder"]');

    form.addEventListener('submit', (e) => {
        const params = new URLSearchParams();
        if (namesInput.value.trim()) params.append('name', namesInput.value.trim());
        if (directorsInput.value.trim()) params.append('director', directorsInput.value.trim());
        if (genresSelect.value) params.append('genre', genresSelect.value);
        if (scoreOrderSelect.value) params.append('scoreOrder', scoreOrderSelect.value);

        const queryString = params.toString();
        const url = `/search?${queryString}`;

        window.location.href = url;
    });
});