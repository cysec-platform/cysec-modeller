import { CategoryModel } from './category-model';
import { ExternalControlModel } from './external-control-model';

export class ControlModel {
  id: number | undefined;
  name: string | undefined;
  details: string | undefined;
  category: CategoryModel | undefined;
  dependenciesCount: number | undefined;
  dependentsCount: number | undefined;
  sourcesCount: number | undefined;
  dependencies: ControlModel[] = [];
  dependents: ControlModel[] = [];
  sources: ExternalControlModel[] = [];
}
