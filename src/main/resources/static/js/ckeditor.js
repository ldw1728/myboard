

class MyUploadAdapter{ //custom UploadAdapter
    constructor(loader){
        this.loader = loader;
    }

    upload(){
        return this.loader.file
        .then( file => new Promise((resolve, reject)=>{
            this._initRequest();
            this._initListeners(resolve, reject, file);
            this._sendRequest(file);
        }));
    }

    abort(){
        if(this.xhr){
            this.xhr.abort();
        }
    }

    _initRequest(){
        const xhr = this.xhr = new XMLHttpRequest();
        xhr.open('POST', 'http://localhost:8888/board/post/upload/image', true);
        xhr.responseType = 'text';
    }

    _initListeners(resolve, reject, file){
        const xhr = this.xhr;
        const loader = this.loader;
        const genericErrorText = "Couldn't upload file.";

        xhr.addEventListener('error', () => reject(genericErrorText));
        xhr.addEventListener('abort', () => reject());
        xhr.addEventListener('load', () => {
            if(xhr.status === 200 || xhr.status === 201){

                console.log(xhr.response);
            }else{
                console.error(xhr.responseText);
            }
            const response = xhr.response;
            
            if(!response || response.error){
                return reject(response && response.error ? 
                    response.error.message : genericErrorText);
                }
            
            resolve({
                default : "/board"+xhr.response
            });
           
        });
        
        if(xhr.upload){
            xhr.upload.addEventListener('progress', evt=>{
                if(evt.lengthComputable){
                    loader.uploadTotal = evt.total;
                    loader.uploaded = evt.loaded;
                }
            });
        }
    }

    _sendRequest(file){
        const data = new FormData();
        data.append('upload', file);
        this.xhr.send(data);
    }   
}

function MyCustomUploadAdapterPlugin(editor){
    editor.plugins.get('FileRepository').createUploadAdapter = (loader)=>{
        return new MyUploadAdapter(loader);
    };
} 


ClassicEditor
    .create(document.querySelector("#content"),{
        extraPlugins: [MyCustomUploadAdapterPlugin]
    })
    .then(editor=>{
       MyCustomUploadAdapterPlugin(editor);
    })
    .catch(error=>{
        console.error(error);
    });

