import {Component, OnInit, Input} from '@angular/core';
import {Acmer} from "../../model/Acmer";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-acmer-details',
  templateUrl: './acmer-details.component.html',
  styleUrls: ['./acmer-details.component.css']
})
export class AcmerDetailsComponent implements OnInit {
  @Input() acmer: Acmer;
  handle: string;

  constructor(private route: ActivatedRoute) {  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.handle = params.handle;
    });
  }

}
