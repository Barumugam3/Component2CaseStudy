db.createUser(
    {
        user: "appUser",
        pwd: "appPass",
        roles: [
            {
                role: "readWrite",
                db: "stock"
            }
        ]
    }
);