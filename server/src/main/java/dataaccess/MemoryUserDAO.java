package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Objects;


public class MemoryUserDAO implements UserDAO{
    private final ArrayList<UserData> userDataSet = new ArrayList<>();

    public MemoryUserDAO(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryUserDAO that = (MemoryUserDAO) o;
        return Objects.equals(userDataSet, that.userDataSet);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userDataSet);
    }

    @Override
    public String toString() {
        return "MemoryUserDAO{" +
                "userDataSet=" + userDataSet +
                '}';
    }

    @Override
    public void clear(){
        if (!userDataSet.isEmpty()){
            userDataSet.clear();

        }
    }

    @Override
    public void createUser(String username, String password, String email){
        UserData newUser = new UserData(username, password, email);
        userDataSet.add(newUser);
    }

    @Override
    public UserData getUser(String username){
        for (UserData user : userDataSet){
            if (Objects.equals(user.username(), username)){
                return user;
            }
        }
        return null;
    }
}
