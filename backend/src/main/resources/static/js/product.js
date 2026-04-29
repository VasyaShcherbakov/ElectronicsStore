const btnToggleType = document.getElementById("btn-toggle-type");
const inpImageFile = document.getElementById("imageFile");
const inpImageUrl = document.getElementById("imageUrl");

btnToggleType.addEventListener("click", () => {
  if (inpImageFile.style.display === "none") {
    inpImageFile.style.display = "inline-block";
    inpImageUrl.style.display = "none";
    inpImageUrl.value = "";
    btnToggleType.textContent = "URL";
  } else {
    inpImageFile.style.display = "none";
    inpImageUrl.style.display = "inline-block";
    inpImageFile.value = "";
    btnToggleType.textContent = "File";
  }
});
