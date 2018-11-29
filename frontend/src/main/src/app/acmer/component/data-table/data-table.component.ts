import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatSort} from '@angular/material';
import { DataTableDataSource } from './data-table-datasource';
import {AcmerService} from "../../service/acmer.service";
import {Router} from '@angular/router';

@Component({
  selector: 'app-data-table',
  templateUrl: './data-table.component.html',
  styleUrls: ['./data-table.component.css']
})
export class DataTableComponent implements OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  dataSource: DataTableDataSource;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['firstName','lastName','email','handle','solvedProblems','rank'];
  constructor(private router: Router, private acmerService: AcmerService) {
  }
  ngOnInit() {
    this.acmerService.getAllAcmers().subscribe(data => {
      this.dataSource.data = data;
    }, error => console.log(error));
    this.dataSource = new DataTableDataSource(this.paginator, this.sort);
  }
}
