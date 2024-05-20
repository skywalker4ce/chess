package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{
    private final ArrayList<AuthData> authDataSet = new ArrayList<>();
    private String genAuthToken = "ChangeThisAuthToken";

    public MemoryAuthDAO(){}

    @Override
    public String toString() {
        return "MemoryAuthDAO{" +
                "authDataSet=" + authDataSet +
                ", genAuthToken='" + genAuthToken + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryAuthDAO that = (MemoryAuthDAO) o;
        return Objects.equals(authDataSet, that.authDataSet) && Objects.equals(genAuthToken, that.genAuthToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authDataSet, genAuthToken);
    }

    @Override
    public void clear(){
        if (!authDataSet.isEmpty()){
            authDataSet.clear();
            System.out.println(authDataSet);
        }
    }

    @Override
    public String createAuth(String username){
        //this should generate a better authToken later on
        AuthData newAuth = new AuthData(genAuthToken, username);
        authDataSet.add(newAuth);
        System.out.println(authDataSet);
        return genAuthToken;
    }

    @Override
    public AuthData getAuth(String authToken){
        for (AuthData auth : authDataSet){
            if (Objects.equals(auth.authToken(), authToken)){
                return auth;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken){
        for (AuthData auth : authDataSet){
            if (Objects.equals(auth.authToken(), authToken)){
                authDataSet.remove(auth);
            }
        }
    }
}
