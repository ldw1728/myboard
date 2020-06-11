
const commentForm = document.getElementById("commentGroup");

let clickBtn = false;

function onClickCommentBtn(){
    if(clickBtn == false){
        clickBtn = true;
        commentForm.style.display = "block";
        
    }

    else{
        clickBtn = false;
        commentForm.style.display = "none";
       
    }
}


function init(){
    commentBtn.onclick = onClickCommentBtn;
    commentForm.style.display = "none";


}
    

init();








