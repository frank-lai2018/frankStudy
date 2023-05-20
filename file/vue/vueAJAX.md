# vue ajax

* 使用vue-resource (1.X版本用)

```javascript
<template>
  <div id="app">
    <div v-if="!dataUrl">Loging...</div>
    <div v-else>github 最受歡迎的專案:,<a :href="dataUrl">{{dataName}}</a></div>
  </div>
</template>

<script>

let url = `https://api.github.com/search/repositories?q=v&sort=stars`
export default {
  data () {
    return {
      dataUrl: '',
      dataName: ''
    }
  },
  mounted () {
    this.$http.get(url).then(
      response => {
        console.log(response)
        this.dataUrl = response.data.items[0].html_url
        this.dataName = response.data.items[0].name
      },
      response => {

      }
    )
  }
}
</script>

<style>
</style>

```

* 使用axios

```javascript
<template>
  <div id="app">
    <div v-if="!dataUrl">Loging...</div>
    <div v-else>github 最受歡迎的專案:,<a :href="dataUrl">{{dataName}}</a></div>
  </div>
</template>

<script>
import axios from 'axios'
let url = `https://api.github.com/search/repositories?q=v&sort=stars`
export default {
  data () {
    return {
      dataUrl: '',
      dataName: ''
    }
  },
  mounted () {
    axios.get(url).then(
      response => {
        this.dataUrl = response.data.items[0].html_url
        this.dataName = response.data.items[0].name
      }
    ).catch(error => {
      alert(error)
    })
  }
}
</script>

<style>
</style>

```