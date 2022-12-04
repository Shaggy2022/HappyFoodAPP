export class Account {
  constructor(
    public activated: boolean,
    public authorities: string[],
    public email: string,
    public langKey: string,
    public login: string,
    public imageUrl: string | null
  ) {}
}
