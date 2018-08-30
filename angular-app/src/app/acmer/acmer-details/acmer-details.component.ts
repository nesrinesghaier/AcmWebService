import { Component, OnInit,Input } from '@angular/core';
import {Acmer} from "../Acmer";

@Component({
  selector: 'app-acmer-details',
  templateUrl: './acmer-details.component.html',
  styleUrls: ['./acmer-details.component.css']
})
export class AcmerDetailsComponent implements OnInit {
  @Input() acmer: Acmer;

  constructor() { }

  ngOnInit() {
  }

}
