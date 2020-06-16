
const commentForm = document.getElementById("commentGroup");
const image = document.getElementsByTagName("img");

function setOnClickImg(){
    for(var i in image){
        image.item(i).onclick = function(){
            window.open(image.item(i).src);
        }
    }
}

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
    //setOnClickImg(); 

}
    
init();








