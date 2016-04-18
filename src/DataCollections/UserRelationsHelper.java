/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCollections;

import datamanagement.UserRelation_dbo;
import datamanagement.User_dbo;
import datamanagement.UsersRelationsTable;
import java.util.ArrayList;


/**
 *
 * @author Srikanth
 */
public class UserRelationsHelper {
    
    
    
    public UserRelation_dbo[] selectUserRelations(boolean[] selected, String whereclause, int min_id, int count){
        return UsersRelationsTable.select(selected, whereclause, min_id, count);
    }
    
    public void insertEdge(User_dbo user1, User_dbo user2){
        Long ntweets;
        UserRelation_dbo relation = checkEdgePair(user1.values[User_dbo.map.get("user_id")].lnumber,user2.values[User_dbo.map.get("user_id")].lnumber);
        if(relation==null){
            relation = checkEdgePair(user2.values[User_dbo.map.get("user_id")].lnumber,user1.values[User_dbo.map.get("user_id")].lnumber);
            if(relation!=null){
               ntweets =  relation.values[UserRelation_dbo.map.get("su_tweets")].lnumber+1;
               relation.values[UserRelation_dbo.map.get("su_tweets")].lnumber = ntweets;
            }
        }
        else{
            ntweets =  relation.values[UserRelation_dbo.map.get("fu_tweets")].lnumber+1;
               relation.values[UserRelation_dbo.map.get("fu_tweets")].lnumber = ntweets;
        }
        if(relation==null) {
        relation = new UserRelation_dbo();
        relation.values[UserRelation_dbo.map.get("fu_id")].setValue(String.valueOf(user1.values[User_dbo.map.get("user_id")].lnumber));
        relation.values[UserRelation_dbo.map.get("su_id")].setValue(String.valueOf(user2.values[User_dbo.map.get("user_id")].lnumber));
        relation.values[UserRelation_dbo.map.get("fu_tweets")].lnumber = Long.valueOf(1);
        }
        UsersRelationsTable.insert(relation);
    }
    
    public UserRelation_dbo checkEdgePair(long user1, long user2){
        UserRelation_dbo relation;
        String whereclause = " (fu_id = "+user1+" and  su_id = "+user2+" )  ";
        UserRelation_dbo[] relations = UsersRelationsTable.select(whereclause, 0, 2);
        if(relations.length>0){
            return relations[0];
        }
        else
        {
            return null;
        }
    }
    
    public UserRelation_dbo[] getEdges(User_dbo user){
        ArrayList<UserRelation_dbo> relationslist = new ArrayList<>();
        String whereclause = " (( fu_id = "+user.values[User_dbo.map.get("user_id")].lnumber+") or (su_id = "+user.values[User_dbo.map.get("user_id")].lnumber+"))";
        boolean available = true;
        long min_id = 0;
        int count = 100;
        while(available){
        UserRelation_dbo[] relations = UsersRelationsTable.select(whereclause, min_id, count);
        if(relations.length==0) {
            available = false;
            continue;
        }
        for(int index =0; index < relations.length; index++) {
        relationslist.add(relations[index]);
        }
        min_id = relations[relations.length-1].values[UserRelation_dbo.map.get("id")].lnumber;
         }
    return (UserRelation_dbo[])relationslist.toArray();
    }
    
}
