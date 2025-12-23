import { Routes } from '@angular/router';
import { Registration } from './layout/registration/registration';
import { Navbar } from './layout/navbar/navbar';
import { Reset } from './layout/reset/reset';

export const routes: Routes = [
    { path: 'navbar', component: Navbar },
    { path: 'register', component: Registration },
    { path: 'reset', component: Reset },
];