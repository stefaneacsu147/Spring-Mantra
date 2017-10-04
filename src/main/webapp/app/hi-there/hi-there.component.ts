import { Component, OnInit } from '@angular/core';
import { Http, Response, Headers, URLSearchParams, RequestOptions } from '@angular/http';
import { Injectable } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { NgModule }      from '@angular/core';



@Injectable()
@Component({
  selector: 'jhi-hi-there',
  templateUrl: './hi-there.component.html',
  styleUrls: [
    'hi-there.css'
  ]
  
})
export class HiThereComponent implements OnInit {

  message: string;
  files=[];

  constructor(private http: Http, private alertService: JhiAlertService ) {
  }

  ngOnInit() {
    this.list();
    
  }
  
  list()
  {
    	this.files=[];
     this.http.get('http:\\\\localhost:8080\\content\\ftp\\list')
        .flatMap((response) => response.json())
        .subscribe((data) => {
        	
        	this.files.push(data);
        });
        console.log(this.files);
    
  }

  doUpload($event) {
    const files = $event.target.files || $event.srcElement.files;
    const file = files[0];

    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    const headers = new Headers();
    headers.append('Access-Control-Allow-Origin', 'http://localhost');


    headers.append('Access-Control-Allow-Methods', 'GET, POST, PATCH, PUT, DELETE, OPTIONS');
    headers.append('Access-Control-Allow-Headers', 'Origin, Content-Type, X-Auth-Token');
    const options = new RequestOptions({ headers: headers });
    const url = 'localhost:8080\\content\\upload';
    this.http.post('http:\\\\localhost:8080\\content\\ftp\\upload', formData, options).subscribe(data => {
		console.log('Upload Successfull');
    	this.list();   
    }, error => {
      console.log(formData);
      console.log(JSON.stringify(error.json()));
    });
    
    


  }

}
