import {Component, Inject, LOCALE_ID, OnInit} from '@angular/core';
import {RegisterService} from '../../services/register.service';
import {ActivatedRoute} from '@angular/router';
import {Bill} from '../../dtos/bill';
import {User} from '../../dtos/user';
import {BillService} from '../../services/bill.service';
import {Register} from '../../dtos/register';
import {AuthService} from '../../services/auth.service';
import {TimeSumBill} from '../../dtos/time-sum-bill';
import {formatDate} from '@angular/common';
import {StorageService} from '../../services/storage.service';
import {NameSum} from '../../dtos/name-sum';

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.scss']
})
export class StatisticComponent implements OnInit {

  error = false;
  date: Date = new Date();
  dateThrownAway: Date = new Date();
  errorMessage = '';
  monthlySum: number;
  sumOfLastTwelveMonths: TimeSumBill[]=[];
  sumOfLastTenYears: TimeSumBill[]=[];
  help: TimeSumBill={sum: 0,date: ''};
  helpThrownAway: TimeSumBill ={sum: 0,date: ''};
  monthlySumThrownAway: number;
  sumOfLastTwelveMonthsThrownAway: TimeSumBill[]=[];
  sumOfLastTenYearsThrownAway: TimeSumBill[]=[];
  mostThrownAwayArticles: NameSum[]=[];
  check: boolean;

  constructor(private registerService: RegisterService, private billService: BillService, public route: ActivatedRoute,
              private authService: AuthService,@Inject(LOCALE_ID) private locale: string, private storageService: StorageService) { }

  ngOnInit(): void {
    this.getSumOfTheWholeYear();
    this.getSumOfLastTenYears();
    this.getSumOfTheWholeYearThrownAway();
    this.getSumOfLastTenYearsThrownAway();
    this.getMostOftenThrownAwayArticles();
    this.check = false;
    this.checkThrownAwayArticles();
  }
  private getSumOfTheWholeYear(){
    this.date = new Date();
    this.date.setDate(1);
    console.log(this.date);
    for (let i = 0; i < 11; i++) {
      this.getSumOfMonthAndYear(this.date);
      this.date.setMonth(this.date.getMonth()-1);
    }
  }
  private checkThrownAwayArticles(){
    this.mostThrownAwayArticles.forEach(value => {
      if(value!==null){
        this.check= true;
      }
    }
    );
  }

  private getSumOfLastTenYears(){
    this.date=new Date();
    console.log(this.date);
    for (let i = 0; i < 10; i++) {
      this.getSumOfSpecificYear(this.date);
      this.date.setFullYear(this.date.getFullYear()-1);
    }
  }

  private getSumOfMonthAndYear(specificDate: Date){
    this.registerService.getSumOfMonthAndYear(formatDate(specificDate,'yyyy-MM-dd',this.locale)).subscribe({
      next: data => {
        console.log('received sum of all Bills in a specific month', data);
        if(data.sum!==0){
          const helpString: string = data.date.charAt(5)+data.date.charAt(6)+'/'
            +data.date.charAt(0)+data.date.charAt(1)+data.date.charAt(2)+data.date.charAt(3);
          const helpSumBill: TimeSumBill = {sum: data.sum, date: helpString};
          this.sumOfLastTwelveMonths.push(helpSumBill);
        }
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  private getSumOfSpecificYear(specificDate: Date){
    this.registerService.getSumOfYear(formatDate(specificDate,'yyyy-MM-dd',this.locale)).subscribe({
      next: data => {
        console.log('received sum of all Bills in a specific year', data);
        if(data.sum!==0){
          const helpString: string = data.date.charAt(0)+data.date.charAt(1)+data.date.charAt(2)+data.date.charAt(3);
          const helpSumBill: TimeSumBill = {sum: data.sum, date: helpString};
          this.sumOfLastTenYears.push(helpSumBill);
        }
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  private getMostOftenThrownAwayArticles(){
    this.storageService.getMostThrownAwayArticles().subscribe({
      next: data => {
        console.log('received sum of all Bills in a specific year', data);
        this.mostThrownAwayArticles = data;
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  private getSumOfTheWholeYearThrownAway(){
    this.dateThrownAway = new Date();
    this.dateThrownAway.setDate(1);
    console.log(this.dateThrownAway);
    for (let i = 0; i < 11; i++) {
      this.getSumOfMonthAndYearThrownAway(this.dateThrownAway);
      this.dateThrownAway.setMonth(this.dateThrownAway.getMonth()-1);
    }
  }
  private getSumOfLastTenYearsThrownAway(){
    this.dateThrownAway=new Date();
    console.log(this.dateThrownAway);
    for (let i = 0; i < 10; i++) {
      this.getSumOfSpecificYearThrownAway(this.dateThrownAway);
      this.dateThrownAway.setFullYear(this.dateThrownAway.getFullYear()-1);
    }
  }

  private getSumOfMonthAndYearThrownAway(specificDate: Date){
    this.storageService.getSumOfArticlesOfSpecificMonth(formatDate(specificDate,'yyyy-MM-dd',this.locale)).subscribe({
      next: data => {
        console.log('received sum of all Bills in a specific month', data);
        if(data.sum!==0){
          const helpStringThrownAway: string = data.date.charAt(5)+data.date.charAt(6)+'/'
            +data.date.charAt(0)+data.date.charAt(1)+data.date.charAt(2)+data.date.charAt(3);
          const helpSum: TimeSumBill = {sum: data.sum, date: helpStringThrownAway};
          this.sumOfLastTwelveMonthsThrownAway.push(helpSum);
        }
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  private getSumOfSpecificYearThrownAway(specificDate: Date){
    this.storageService.getSumOfArticlesOfSpecificYear(formatDate(specificDate,'yyyy-MM-dd',this.locale)).subscribe({
      next: data => {
        console.log('received sum of all Bills in a specific year', data);
        if(data.sum!==0){
          const helpString: string = data.date.charAt(0)+data.date.charAt(1)+data.date.charAt(2)+data.date.charAt(3);
          const helpSumBill: TimeSumBill = {sum: data.sum, date: helpString};
          this.sumOfLastTenYearsThrownAway.push(helpSumBill);
        }
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }
}

