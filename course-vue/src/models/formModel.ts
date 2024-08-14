import {OptionItem} from "~/models/general";

export interface FormModel{
    label:string,
    labelWidth:number,
    itemType:string,
    labelKey:string,
    formKey:string,
    optionKey:string
}
export interface FromOption{
    labelKey:string,
    option: OptionItem[]
}