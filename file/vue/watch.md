# 11.watch

vue物件裡哦watch屬性監視數據變動，有變動才會被監測到，故可以用來監測html tag 的value變動，或者監視router連結的變動

*   監視input tag value的變動

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
    <div id="app">
        <input type="text" v-model="firstName"> +
        <input type="text" v-model="lastName" > =
        <input type="text" v-model="fullName">
    </div>
</body>
<script src="./lib/vue.js"></script>

<script>
    let vm = new Vue({
        el:"#app",
        data() {
            return {
                firstName:"",
                lastName:"",
                fullName:""
            }
        },
        methods: {

        },
        watch: {
            firstName(newVal,oldVal){
                console.log(newVal,"--",oldVal);
                this.fullName =newVal +this.lastName;
            },
            lastName(newVal,oldVal){
                console.log(newVal,"--",oldVal);
                this.fullName =this.firstName +oldVal;
            }
        },
    })
</script>
</html>
```

*   監視router url變動

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
    <div id="app">
        <router-link to="/login">login</router-link>
        <router-link to="/register">register</router-link>
        <router-view></router-view>
    </div>

    <template id= "login">
        <div>
            <h1>login</h1>
        </div>
    </template>
    <template id= "register">
        <div>
            <h1>register</h1>
        </div>
    </template>
</body>

<script src="./lib/vue.js"></script>
<script src="./lib/vue-router.js"></script>
<script>

    let login = {
        template:"#login"
    };
    let register = {
        template:"#register"
    };

    let router = new VueRouter({
        routes:[
            {path:"/login",component:login},
            {path:"/register",component:register}

        ],
    });

    let vm = new Vue({
        el:"#app",
        data() {
            return {

            }
        },
        methods: {
            watchPath(path) {
                if(path === "/login"){
                    console.log("您正在登入...");
                }else if(path === "/register"){
                    console.log("您正在註冊...");
                }
            },
        },
        watch: {
            "$route.path"(newValue,oldValue){
                if(newValue === "/login"){
                    console.log("您正在登入...");
                }else if(newValue === "/register"){
                    console.log("您正在註冊...");
                }
            }
        },

        router

    });
</script>
</html>
```

# 另一種方式


```html
<template>
  <div class="redeemVoucherContent">
    <div class="redeemVoucher_header"><i class="glyphicon glyphicon-gift"></i>輸入配送資訊</div>
    <div class="redeemShip_container">
      <div class="redeemShip_header orderItem_header">
        <div class="orderItem_header_left">
          <div class="prodImg_block">
            <img :src="shipInfoForm.prodPicUrl" :onerror="errorImage" />
            <img class="space" src="@/assets/images/space.gif" />
          </div>
        </div>
        <div class="orderItem_header_right">
          <div class="orderItem_header_mid">
            <div class="prodName_block">{{shipInfoForm.prodName}}</div>
            <div class="prodPrice_block small">市價 NT${{shipInfoForm.proMvalue}}</div>
            <div class="prodSpec_block small">規格 {{shipInfoForm.prodSize}}</div>
          </div>
        </div>
      </div>
      <div class="divider-dot"></div>
      <v-form ref="form" id="shipInfoForm">
        <div class="form-group">
          <label>
            <h4>收件人姓名</h4>
          </label>
          <input class="form-control"
            type="text"
            v-model="shipInfoForm.osiCname"
            placeholder="請輸入收件人姓名"
            data-parsley-required
            data-parsley-required-message="請輸入收件人姓名" />
        </div>
        <div class="form-group">
          <label>
            <h4>行動電話</h4>
          </label>
          <input
            class="form-control"
            type="tel"
            maxlength="10"
            pattern="[0-9]*"
            placeholder="請輸入行動電話"
            v-model="shipInfoForm.osiCmtel"
            data-parsley-required
            data-parsley-required-message="請輸入行動電話"
            data-parsley-phone-number
            data-parsley-phone-number-message="請輸入正確的行動電話(限09開頭)"
          />
        </div>
        <div class="form-group">
          <label>
            <h4>
              電子信箱
            </h4>
          </label>
          <input class="form-control" type="email"
            v-model="shipInfoForm.osiCemail"
            placeholder="請輸入電子信箱"
            data-parsley-required
            data-parsley-required-message="請輸入電子信箱"
            data-parsley-type="email"
            data-parsley-type-message="請輸入正確的電子信箱" />
        </div>
        <div class="form-group">
          <label>
            <h4>
              縣市區域
            </h4>
          </label>
          <div class="adressSelect_block form-inline clearfix">
            <select class="form-control"
              v-model="shipInfoForm.osiCity"
              data-parsley-required
              data-parsley-required-message="請選擇縣市"
              @change="cleanOsiArea">
              <option value="">請選擇縣市</option>
              <option :value="city" v-for="(city, cityIndex) in shipInfoForm.citys" :key="cityIndex">{{city}}</option>
            </select>
            <select class="form-control"
              data-parsley-required
              data-parsley-required-message="請選擇區域"
              v-model="shipInfoForm.osiArea">
              <option value="">請選擇區域</option>
              <option :value="postalCodeItem.area ? postalCodeItem.area:postalCodeItem.city" v-for="(postalCodeItem, postalCodeItemIndex) in areas" :key="postalCodeItemIndex">{{postalCodeItem.area ? postalCodeItem.area:postalCodeItem.city}}</option>
            </select>
          </div>
        </div>
        <div class="form-group">
          <label>
            <h4>
              收件地址
            </h4>
          </label>
          <input class="form-control"
          type="text"
          placeholder="請輸入收件地址"
          data-parsley-required
          data-parsley-required-message="請輸入收件地址"
          :data-parsley-ship-addr="`${shipInfoForm.osiCity}${shipInfoForm.osiArea}`"
          data-parsley-ship-addr-message="請輸入收件地址"
          v-model="shipInfoForm.osiCaddr"/>
        </div>
        <div class="form-group text-center">
          <label class="control-label">
            <input type="checkbox"
              name="shipInfoForm.checkPersonalInfo"
              v-model="shipInfoForm.checkPersonalInfo"
              data-parsley-required
              data-parsley-required-message="請勾選"
            />已閱讀
            <router-link to="/infoview/term_privacy" target="_blank">個資聲明</router-link>
          </label>
        </div>
        <div class="form-group form-group-lg">
          <button type="submit" class="btn btn-primary btn-lg btn-block" @click.prevent="validate">確認兌換</button>
        </div>
      </v-form>
    </div>
    <!-- model -->
    <v-modal ref="checkRedeemModal" v-model="checkRedeemModal" title="確定兌換" customClass="modal-sm">
      <slot name="default">
        <div class="text-center">
          請確認配送資訊，<br />兌換後將無法修改，<br />
          若確認沒問題，點選我要兌換
          <br />
          <br />
          <div class="btn-group">
            <button class="btn btn-default" @click="close()">重新確認</button>
            <button class="btn btn-default" @click="submit">我要兌換</button>
          </div>
        </div>
      </slot>
    </v-modal>
    <!-- model END -->
  </div>
</template>
<script>
export default {
  name: 'shipinfo',
  data: function() {
    return {
      checkRedeemModal: false,
      errorImage: 'this.src="' + require('@/assets/images/notExist.jpg') + '"',
      shipInfoForm:{
          osiCaddr:'',
          osiArea:'',
          osiCity:'',
          osiCemail:'',
          osiCmtel:'',
          osiCname:'',
          prodName:'',
          proMvalue:'',
          prodSize:''
      }
    }
  },
  created: function() {
    let vm = this
  },
  watch: {
    'shipInfoForm.osiCity':{
      handler:function(){
        this.shipInfoForm.osiCaddr =  this.shipInfoForm.osiCity+this.shipInfoForm.osiArea
      }
    },
    'shipInfoForm.osiArea':{
      handler:function(){
        this.shipInfoForm.osiCaddr =  this.shipInfoForm.osiCity+this.shipInfoForm.osiArea
      }
    }
  },
  computed: {
    areas() {
      let vm = this
      let postalCodeItems=vm.shipInfoForm.postalCodeItems
      let newPostalCodeItems=postalCodeItems.filter(postalCodeItem => postalCodeItem.city === vm.shipInfoForm.osiCity);
      return newPostalCodeItems
    }
  },
  methods: {
    goShipProgress: function() {
      let vm = this
      vm.$router.push('/shipprogress')
    },
    close: function() {
      this.$refs.checkRedeemModal.close()
    },
    cleanOsiArea: function(){
      let vm = this
      vm.shipInfoForm.osiArea = ''
    },
    validate: function(){
    },
    submit: function(){
    }
  }
}
</script>

```