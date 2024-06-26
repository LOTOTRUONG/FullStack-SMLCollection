// HeaderFunction.js

export function setUserDropdownListener() {
  const userAvatar = document.querySelector('.header__avatar');

  if (userAvatar) {
    userAvatar.addEventListener('click', function(e) {
      const dropdown = this.querySelector('.dropdown');
      if (dropdown) {
        dropdown.classList.toggle('dropdown--active');
      }
    });
  } else {
    console.error("User avatar element not found.");
  }
}

