import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SearchView from '../views/SearchView.vue'
import BoardDetailView from '../views/BoardDetailView.vue'
import PTReservationView from '../views/PTReservationView.vue'
import BoardCreateView from '../views/BoardCreateView.vue'
import BoardEditView from '../views/BoardEditView.vue'

//로그인
import LoginView from '../views/LoginView.vue'
import SignupView from '../views/SignupView.vue'
import SignupSuccessView from '@/views/SignupSuccessView.vue'
//프로필
import ProfilePage from '../views/ProfilePage.vue'
import ProfileEditPage from '../views/ProfileEditPage.vue'
import ChangePasswordPage from '../views/ChangePasswordPage.vue'
import MyPostsPage from '../views/MyPostsPage.vue'



const routes = [
  { path: '/', name: 'home', component: HomeView },
  { path: '/board/:boardId', name: 'boardDetail', component: BoardDetailView },
  {
  path: '/board/edit/:boardId',
  name: 'BoardEdit',
  component: BoardEditView
},

  { path: '/search', name: 'search', component: SearchView },
  { path: '/board/:boardId', name: 'board-detail', component: BoardDetailView },
  { path: '/reservation/pt', name: 'pt', component: PTReservationView },
  { path: '/board/create', name: 'board-create', component: BoardCreateView },
  { path: '/auth', name: 'login', component: LoginView },
  { path: '/signup', name: 'signup', component: SignupView },
  { path: '/signup/success', name: 'signup-success', component: SignupSuccessView },
  {path: '/profile', name: 'profile', component: ProfilePage},
  {path: '/profile/password/:userId', name: 'change-password', component: ChangePasswordPage},
  {
    path: '/change-password/step2/:userId',
    name: 'change-password-step2',
    component: () => import('@/views/ChangePasswordStep2.vue')
  },

  {path: '/my-posts', name: 'my-posts', component: MyPostsPage},
  {
    path: '/gym-search',
    name: 'gym-search',
    component: () => import('@/views/GymSearchPage.vue')
  },
  {
    path: '/trainer-search',
    name: 'trainer-search',
    component: () => import('@/views/TrainerSearchPage.vue')
  },
  {
    path: '/profile/edit/:userId',
    name: 'profile-edit',
    component: () => import('@/views/ProfileEditPage.vue')
  },

  {
  path: '/my-reservations',
  name: 'my-reservations',
  component: () => import('@/views/MyReservationView.vue')
},


{
  path: '/equipment-reservation',
  name: 'equipment-reservation',
  component: () => import('@/views/EquipmentReservationPage.vue'),
  beforeEnter: (to, from, next) => {
    const token = sessionStorage.getItem('access-token')
    if (!token) {
      alert('로그인 하세요')
      next({ name: 'login' })
    } else {
      next()
    }
  },
  children: [
    {
      path: '',
      name: 'equipment-calendar',
      component: () => import('@/views/reservation/Step1Calendar.vue')
    },
    {
      path: 'select',
      name: 'equipment-select',
      component: () => import('@/views/reservation/Step2SelectEquipment.vue')
    },
    {
      path: 'time',
      name: 'equipment-time',
      component: () => import('@/views/reservation/Step3SelectTime.vue')
    }
  ]
}


]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
