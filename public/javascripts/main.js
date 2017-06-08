$(document).ready(function(){
  $(".del").click(function(){
    if (!confirm("Wirklich Löschen? Alle zugehörigen Daten werden gelöscht.")){
      return false;
    }
  });
});