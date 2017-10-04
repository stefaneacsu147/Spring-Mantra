import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HI_THERE_ROUTE, HiThereComponent } from './';

@NgModule({
    imports: [
        RouterModule.forRoot([HI_THERE_ROUTE], { useHash: true }),
        CommonModule
    ],
    declarations: [
        HiThereComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterHiThereModule { }
