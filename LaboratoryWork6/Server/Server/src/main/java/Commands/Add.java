package Commands;

import CommandData.ClientData;
import CommandData.InputCommandData;
import DataTypes.Coordinates;
import DataTypes.MusicBand;
import DataTypes.MusicGenre;
import DataTypes.Person;
import Main.CollectionManager;
import Utils.Client;
import Utils.IdManager;
import Utils.Printer;

import java.util.ArrayList;
import java.util.LinkedList;

public class Add extends Command {
    @Override
    public void execute(InputCommandData input) {
        Client client = input.client();
        ClientData clientData = input.clientData();
        Printer printer = input.printer();
        ArrayList<Object> args = input.clientData().getArgs();
        CollectionManager collectionManager = input.collectionManager();
        IdManager idManager = collectionManager.getIdManager();
        LinkedList<MusicBand> musicBands = collectionManager.getMusicBands();
        Long id = idManager.add();
        MusicBand musicBand = new MusicBand(id,
                (String) args.get(0),
                (Coordinates) args.get(1),
                (java.util.Date) args.get(2),
                (long) args.get(3),
                (long) args.get(4),
                (MusicGenre) args.get(5),
                (Person) args.get(6));
        musicBands.add(musicBand);
        printer.outPrint("Элемент успешно добавлен", client, clientData);
    }
}
