package app.trendingthings;

/**
 * Created by Liviu on 5/7/2015.
 */
public interface Constants {
    //Gift Columns
    public final String GiftObject = "Gift";
    public final String GiftName = "Name";
    public final String GiftDescription = "Description";
    public final String GiftCategory = "CategoryId";
    public final String GiftPersonSex = "PersonSex";
    public final String GiftUser = "User";
    public final String GiftPicture = "Picture";
    public final String GiftAge = "Age";
    public final String GiftPrice = "Price";
    //

    //GiftCategory
    public final String Categories = "GiftCategories";
    public final String CategoryDescription = "GiftCategoryDescription";
    public final String CategoryId = "GiftCategoryId";
    //

    //Group
    public final String GroupObject = "BirthdayGroup"; //obiect DB
    public final String GroupId = "objectId";
    public final String GroupName = "Name";
    public final String GroupDescription = "Description";
    public final String GroupDate = "Date";
    public final String GropuRelationComments = "Comments";
    //

    //GroupComment
    public final String CommentObject = "CommentInGroup"; //object DB
    public final String CommentContent = "Content";
    //

    //Suggested Category
    public final String SuggestedCategoryObject = "SuggestedCategory"; //obiect DB
    public final String SuggestedCategoryName = "Name";
    //

    //Invite to Group
    public final String InviteObject = "Invite";
    public final String InviteGroupId = "GroupId";
    public final String InviteUsername = "Username";
    public final String InviteByUser = "InvitedByUser";
    public final String InviteGroupName = "GroupName";
    //

    //Intent Data
    public final String GroupToView = "GroupToViewOBjectId";
    //

    //User
    public final String UserGroups = "Groups";
    //


    //
    public final String TestObject = "TestObject1";
}
