ClassicEditor
    .create(document.querySelector("#content"),{
        enterMode:'2'
    })
    
    .catch(error=>{
        console.error(error);
    });