package cleiton.agenda.util;

import com.firebase.client.Firebase;

/**
 * Created by Cleiton Gonçalves on 18/04/2016.
 */
public class FireBaseUtil {
    //Classe para prover conexao com firebase
    private static Firebase firebase;
    private static final String URL ="https://cleitonapp.firebaseio.com/";
    /**
     *
     * @return  retorna conexao com firebase se a conexao ja exirtir mantem a mesma
     */
    public static Firebase getFirebase(){
        if( firebase == null ){
            firebase = new Firebase(URL);
        }
        return( firebase );
    }
}
