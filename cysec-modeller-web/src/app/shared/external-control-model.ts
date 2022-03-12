import { ExternalSourceModel } from './external-source-model';
import { ControlModel } from './control-model';

export class ExternalControlModel {
  id: number | undefined;
  key: string | undefined;
  name: string | undefined;
  source: ExternalSourceModel | undefined;
  dependentsCount: number | undefined;
  dependents: ControlModel[] = [];
}
