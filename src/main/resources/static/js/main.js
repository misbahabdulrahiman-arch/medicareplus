document.addEventListener("DOMContentLoaded", function () {

    const searchInput = document.querySelector(".search-field input");
    const specialtySelect = document.querySelector(".doctor-search select");
    const doctorCards = document.querySelectorAll(".doctor-card");

    function filterDoctors() {

        const searchValue = searchInput.value.toLowerCase().trim();
        const selectedSpecialty = specialtySelect.value.toLowerCase();

        doctorCards.forEach(function (card) {

            const name = card.querySelector("h3").textContent.toLowerCase();
            const specialty = card.querySelector("p").textContent.toLowerCase();

            const searchMatch =
                name.includes(searchValue) ||
                specialty.includes(searchValue);

            const specialtyMatch =
                selectedSpecialty === "all specialties" ||
                specialty === selectedSpecialty;

            if (searchMatch && specialtyMatch) {
                card.style.display = "flex";
            } else {
                card.style.display = "none";
            }

        });

    }

    searchInput.addEventListener("input", filterDoctors);
    specialtySelect.addEventListener("change", filterDoctors);

});