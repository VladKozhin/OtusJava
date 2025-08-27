package classes;

import interfaces.ICommand;

public class TestClass implements ICommand {
    private String msg;
    public TestClass(String msg){
        this.msg = msg;
    }
    @Override
    public void execute() {
        System.out.println(msg);
    }
}
