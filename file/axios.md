# 下載檔案

```javascript
    axios

      .request({

        url,

        method,

        responseType: 'blob', //important

      })

      .then(({ data }) => {

        const downloadUrl = window.URL.createObjectURL(new Blob([data]));

        const link = document.createElement('a');

        link.href = downloadUrl;

        link.setAttribute('download', 'file.zip'); //any other extension

        document.body.appendChild(link);

        link.click();

        link.remove();

      });

  };
```