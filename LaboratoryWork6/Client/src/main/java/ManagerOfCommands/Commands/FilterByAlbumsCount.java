package ManagerOfCommands.Commands;

import ManagerOfCommands.CommandData.ClientData;
import Utils.PrimitiveTypeAsserter;

import java.util.ArrayList;

public class FilterByAlbumsCount extends Command{
    @Override
    public ClientData processing(String title, ArrayList<String> args) {
        ClientData clientData = new ClientData();
        clientData.setName("filter_by_albums_count");
        if(args.size() == 1 && PrimitiveTypeAsserter.checkInteger(args.get(0))) {
            ArrayList<Object> argument = new ArrayList<>();
            argument.add((Object)Integer.parseInt(args.get(0)));
            clientData.setArgs(argument);
            return clientData;
        }else{
            printer.errPrintln("Введены некоректные данные");
            return null;
        }
    }
}
