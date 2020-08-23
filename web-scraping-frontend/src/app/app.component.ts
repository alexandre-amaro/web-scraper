import { Component, OnInit } from '@angular/core';
import { ListModel } from './model/list.model';
import { RequestModel } from './model/request.model';
import { ScrapService } from './service/scrap.service';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { ResponseModel } from './model/response.model';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { interval } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { NzTreeNodeOptions } from 'ng-zorro-antd';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Web Scraper - aamaro.com';

  validateForm: FormGroup;
  urlRegex = /^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\(\)\*\+,;=.]+$/;

  list = new Array<ListModel>();
  request = new RequestModel();
  isLoading = false;
  isLoadingDetails = false;
  detailsFor = 'Links';
  response: ResponseModel;

  levels: object[];
  nodes: NzTreeNodeOptions[];
  
  constructor(private scrapService: ScrapService,
    private notification: NzNotificationService) {
  }

  ngOnInit() {

    this.isLoading = true;

    //set the defaults
    this.request.maxLevels = 1;
    this.request.url = '';

    //levels
    this.levels = [
      {value: 1, label: '1'},
      {value: 2, label: '2'},
      {value: 3, label: '3'}
    ];  

    this.scrapService.list().subscribe(
      (data)=> {
        this.list = [...data];
        this.isLoading = false;
      }, 
      (err) => {
        this.notification.create('error', 'Error', err);
        this.isLoading = false;
      }
    );

    //polling
    interval(5000)
    .pipe(
      startWith(0),
      switchMap(() => this.scrapService.poll(this.list))
    )
    .subscribe(
      (data)=> {
        let listItems = this.list.filter(item => item.status == 'LOADING');
        listItems.forEach(item => {
          let pollItems = data.filter(pollItem => pollItem.url == item.url);
          if (pollItems.length > 0) {
            item.status = pollItems[0].status;
            item.id = pollItems[0].id;
            item.createdOn = pollItems[0].createdOn;
          }
        });        
      }, 
      (err) => {
        this.notification.create('error', 'Error', err);
      }      
    );

  }  
  
  submit(): void {
    
    let listItems = this.list.filter(item => item.url != this.request.url);
    this.list = [...listItems];

    this.scrapService.request(this.request).subscribe(
      (data)=> {        
        this.notification.create("info", "Please wait", "Request being processed");
        let item = new ListModel();
        item.url = this.request.url;
        item.status = "LOADING";
        this.addToList(item);      
      }, 
      (err) => {
        this.notification.create('error', 'Error', err);
      }
    );
  }

  view(id: number): void {
    console.log(id);
    this.isLoadingDetails = true;
    this.detailsFor = 'Loading links from database...';
    this.scrapService.view(id).subscribe(
      (data)=> {
        console.log(data);
        this.detailsFor = 'Links for ' + data['title'];
        this.nodes = [...data['children']];
        this.isLoadingDetails = false;
      }, 
      (err) => {
        this.notification.create('error', 'Error', err);        
        this.isLoadingDetails = false;
      }
    );    
  }

  convert(): void {

    //if the amount is not populated, set the value to 1

    /*
    if (!this.conversion.amount) {
      this.conversion.amount = 1;
    }

    this.response = null;

    let that = this;
    that.isLoading = true;
    this.converterService.convert(this.conversion).subscribe(

      function(res: ResponseModel) {
        res.dateTime = new Date();
        that.response = res;

        let history = new HistoryModel();
        history.to = that.conversion.to;
        history.from = that.conversion.from;
        history.amount = that.conversion.amount;    
        history.result = that.response.result;
        history.dateTime = that.response.dateTime;
        that.addToHistory(history);    
        that.isLoading = false;
      },
      function (err) {
        //on error display a notification to the user
        console.log(err);
        that.isLoading = false;
        that.response = null;
        that.notification.create('error', 'Error', err);            
      }
    );
    */
  }

  addToList(item: ListModel) {
    let table = this.list;    
    table.unshift(item);
    this.list = [...table];
  }
}
