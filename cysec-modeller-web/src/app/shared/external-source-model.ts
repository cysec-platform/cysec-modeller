import { ExternalControlModel } from './external-control-model';

export class ExternalSourceModel {
  id: number | undefined;
  key: number | undefined;
  name: string | undefined;
  controls: ExternalControlModel[] = [];
}
