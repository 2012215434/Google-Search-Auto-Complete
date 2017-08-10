/**
 * Created by zhangsheng on 8/9/17.
 */
function requestForCompletion(startPhrase) {
    $.ajax({
        url: "/completions/" + startPhrase,
        type: "GET",
        dataType: "JSON",
        success: function (data, status) {

            if (status == "success") {

                var availableTags = [];
                for (var i = 0; i < data.length; i++) {
                    availableTags.push(data[i].startPhrase + " " + data[i].followingWord);
                }
                console.log(availableTags);
                $("#startPhrase").autocomplete({source: availableTags});
            }

        }
    });
}

var element = document.getElementById("startPhrase");
element.addEventListener("keyup",function(){
    requestForCompletion(this.value);
});