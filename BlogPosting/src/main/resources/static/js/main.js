import Vue from 'vue'
import Vuetify from 'vuetify'
import 'api/resource.js'
import App from 'pages/App.vue'
import { connect } from './util/ws'

import 'vuetify/dist/vuetify.min.css'


if (frontEndData.profile) {
  connect()
}


Vue.use(Vuetify)

new Vue({
  el: '#app',
  render: a => a(App)
})
