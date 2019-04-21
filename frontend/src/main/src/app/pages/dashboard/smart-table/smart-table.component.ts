import {Component, OnInit} from '@angular/core';
import {LocalDataSource} from 'ng2-smart-table';

import {SmartTableData} from '../../../@core/data/smart-table';
import {AcmerService} from '../../../services/acmer.service';
import {User} from '../../../@core/data/users';

@Component({
  selector: 'ngx-smart-table',
  templateUrl: './smart-table.component.html',
  styles: [`
    nb-card {
      transform: translate3d(0, 0, 0);
    }
  `],
})
export class SmartTableComponent implements OnInit {

  settings = {
    add: {
      addButtonContent: '<i class="nb-plus"></i>',
      createButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
    },
    edit: {
      editButtonContent: '<i class="nb-edit"></i>',
      saveButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
    },
    delete: {
      deleteButtonContent: '<i class="nb-trash"></i>',
      confirmDelete: true,
    },
    columns: {
      id: {
        title: '',
        type: 'number',
      },
      handle: {
        title: 'Handle',
        type: 'string',
      },
      firstName: {
        title: 'First Name',
        type: 'string',
      },
      lastName: {
        title: 'Last Name',
        type: 'string',
      },
      email: {
        title: 'E-mail',
        type: 'string',
      }
    },
  };

  source: LocalDataSource = new LocalDataSource();
  acmers: User[];

  constructor(private service: SmartTableData, private acmerService: AcmerService) {
    const data = this.service.getData();
    console.log('constructed');
    console.log(this.acmerService.apiUrl);
    this.acmerService.getAllAcmers().subscribe(dat => {
      console.log(JSON.stringify(dat));
      //this.source.load(data);
    });
    this.source.load(data);
  }

  onDeleteConfirm(event): void {
    if (window.confirm('Are you sure you want to delete?')) {
      event.confirm.resolve();
    } else {
      event.confirm.reject();
    }
  }

  ngOnInit(): void {
  }
}
