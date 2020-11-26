$(document).ready(function() {
    $.ajax({
        url: "/movement"
    }).done(function(data) {
        console.log(data);
        $('.greeting-id').append(data.id);
        $('.greeting-content').append(data.destRow);
    });
});