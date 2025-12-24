import { Routes } from '@angular/router';
import { Navbar } from './layout/navbar/navbar';
import { RidesList } from './rides/rides-list/rides-list';


export const routes: Routes = [
    {
        path: 'frontend\Gorki\src\app\layout\navbar',
        component: Navbar
    },
    {
        path:'',
        component:RidesList
    }
];
