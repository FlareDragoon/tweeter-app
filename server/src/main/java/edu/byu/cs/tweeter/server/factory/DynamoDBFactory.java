package edu.byu.cs.tweeter.server.factory;

import edu.byu.cs.tweeter.server.dao.DynamoDAO.FollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.StatusDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.UserDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAOInterface;
import edu.byu.cs.tweeter.server.dao.StatusDAOInterface;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;

public class DynamoDBFactory implements FactoryInterface {
    @Override
    public FollowDAOInterface getFollowDAO() {
        return new FollowDAO();
    }

    @Override
    public UserDAOInterface getUserDAO() {
        return new UserDAO();
    }

    @Override
    public StatusDAOInterface getStatusDAO() {
        return new StatusDAO();
    }
}
