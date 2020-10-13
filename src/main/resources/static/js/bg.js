const body = document.querySelector("body"); //랜덤으로 배경화면을 바뀌게 하는 함수인데 현재 안씀.

const IMG_NUMBER = 5;


function paintImage(imgNumber){
    const image = new Image();
    image.src = `/img/${imgNumber+1}.jpg`;
    image.classList.add("bg");
    body.prepend(image);  

}

function genRandom(){
    const number = Math.floor(Math.random()*IMG_NUMBER);
    return number;
}

function init(){
    const randomNumber = genRandom();
    paintImage(randomNumber);
}

init();