package it.polimi.ingsw.client;

public class ClientCLI extends Client{
    public ClientCLI(String ip, int port) {
        super(ip, port);
    }
    @Override
    public void startView()
    {
        ViewCLI viewCLI= new ViewCLI(this);
        viewCLI.Start();
    }
}
