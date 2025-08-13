function changeLanguage(lang) {
  // redirect to recent URL
  // const url = new URL(window.location);
  // url.searchParams.set('lang', lang);
  // window.location.href = url.toString();

  //redirect to home page
  window.location.href = "/?lang=" + lang;
}

