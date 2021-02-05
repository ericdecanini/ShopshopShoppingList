import sqlite3
import os.path
import json

TABLE_SHOPPINGLISTS = "shoppinglists"
TABLE_SHOPITEMS = "shopitems"

package_dir = os.path.abspath(os.path.dirname(__file__))
db_dir = os.path.join(package_dir, 'shopping_lists.db')


class Commands:

    connection = sqlite3.connect(db_dir)
    cursor = connection.cursor()

    def __init__(self):
        create_shoppinglists_table = f"""
        CREATE TABLE IF NOT EXISTS
        {TABLE_SHOPPINGLISTS}(list_id INTEGER PRIMARY KEY, name TEXT NOT NULL)
        """
        self.cursor.execute(create_shoppinglists_table)

        create_shopitems_table = f"""
            CREATE TABLE IF NOT EXISTS
            {TABLE_SHOPITEMS} (
            item_id INTEGER PRIMARY KEY,
            list_id INTEGER NOT NULL,
            name TEXT NOT NULL,
            quantity INTEGER NOT NULL,
            checked INTEGER NOT NULL,
            FOREIGN KEY(list_id) REFERENCES {TABLE_SHOPPINGLISTS}(list_id)
            )
        """
        self.cursor.execute(create_shopitems_table)


    def get_shoppinglists(self):
        self.cursor.execute(
            f"SELECT * FROM {TABLE_SHOPPINGLISTS}"
        )
        return json.dumps(self.cursor.fetchall())

    def get_shoppinglist_by_id(self, list_id):
        self.cursor.execute(
            f"SELECT * FROM {TABLE_SHOPPINGLISTS} WHERE list_id = {list_id}"
        )
        return json.dumps(self.cursor.fetchall())


    def get_shopitems(self, list_id):
        self.cursor.execute(
            f"SELECT * FROM {TABLE_SHOPITEMS} WHERE list_id = {list_id}"
        )
        return json.dumps(self.cursor.fetchall())


    def insert_shoppinglist(self, name):
        self.cursor.execute(
            f"INSERT INTO {TABLE_SHOPPINGLISTS} VALUES (NULL, '{name}')"
        )
        self.connection.commit()


    def insert_shopitem(self, list_id, name, quantity, checked):
        self.cursor.execute(
            f"INSERT INTO {TABLE_SHOPITEMS} VALUES (NULL, {list_id}, '{name}', {quantity}, {checked})"
        )
        self.connection.commit()


    def update_shoppinglist(self, list_id, name):
        self.cursor.execute(
            f"UPDATE {TABLE_SHOPPINGLISTS} SET name = '{name}' WHERE list_id = {list_id}"
        )
        self.connection.commit()


    def update_shopitem(self, item_id, name, quantity, checked):
        self.cursor.execute(
            f"""
            UPDATE {TABLE_SHOPITEMS}
            SET name = '{name}', quantity = {quantity}, checked = {checked}
            WHERE item_id = {item_id}
            """
        )
        self.connection.commit()


    def delete_shoppinglist(self, list_id):
        self.cursor.execute(
            f"DELETE FROM {TABLE_SHOPPINGLISTS} WHERE list_id = {list_id}"
        )
        self.connection.commit()


    def delete_shopitem(self, item_id):
        self.cursor.execute(
            f"DELETE FROM {TABLE_SHOPITEMS} WHERE item_id = {item_id}"
        )
        self.connection.commit()


    def clear_all(self):
        self.cursor.execute(f"DROP TABLE {TABLE_SHOPPINGLISTS}")
        self.cursor.execute(f"DROP TABLE {TABLE_SHOPITEMS}")
        self.connection.commit()


    def cleanup(self):
        self.cursor.close()
        self.connection.close()
