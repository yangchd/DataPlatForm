import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello.vue'
import CarouselSingle from '@/components/show/carousel-single.vue'
import CarouselMultiple from '@/components/show/carousel-multiple.vue'
import NavigationLeft from '@/components/navigation/navigation-left.vue'

Vue.use(Router);

export default new Router({
  routes: [    {
      path: '/hello',
      name: 'Hello',
      component: Hello
    },{
      path: '/carousel-single',
      name: 'CarouselSingle',
      component: CarouselSingle
    },{
      path: '/carousel-multiple',
      name: 'CarouselMultiple',
      component: CarouselMultiple
    },{
      path: '/utils',
      name: 'NavigationLeft',
      component: NavigationLeft
    }
  ]
})
