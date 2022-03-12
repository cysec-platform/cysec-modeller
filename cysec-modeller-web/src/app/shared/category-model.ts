import { ControlModel } from './control-model';

export class CategoryModel {
  id: number | undefined;
  name: string | undefined;
  controls: ControlModel[] = [];
}
