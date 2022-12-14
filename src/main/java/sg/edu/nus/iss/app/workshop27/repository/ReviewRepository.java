package sg.edu.nus.iss.app.workshop27.repository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.app.workshop27.models.EditedComment;
import sg.edu.nus.iss.app.workshop27.models.Review;

@Repository
public class ReviewRepository {

    private static final String REVIEWS_COL = "reviews";

    @Autowired
    private MongoTemplate mongoTemplate;

    public Review insertReview(Review r) {
        return mongoTemplate.insert(r, REVIEWS_COL);
    }

    public Review updateEdits(String _id, EditedComment c) {
        ObjectId docId = new ObjectId(_id);
        Review r = mongoTemplate.findById(docId, Review.class, REVIEWS_COL);
        if (r != null) {
            // if r has existing record, create new EditedComment object
            EditedComment e = new EditedComment();
            //  get comments and ratings from input and set it to the new EditedComment object
            e.setComment(c.getComment());
            e.setRating(c.getRating());
            // Time will be now (when you press update button)
            e.setPosted(LocalDateTime.now());
            if (r.getEdited() != null) {
                // Append new comments into List of EditedComment
                r.getEdited().add(e);
            } else {
                // if r has no existing record, create new
                List<EditedComment> ll = new LinkedList<EditedComment>();
                // add the EditedComment object
                ll.add(e);
                // set the List of EditedComment
                r.setEdited(ll);
            }

            mongoTemplate.save(r, REVIEWS_COL);
        }
        return r;
    }

    public Review getReview(String _id) {
        ObjectId docId = new ObjectId(_id);
        return mongoTemplate.findById(docId, Review.class, REVIEWS_COL);

    }

   
}