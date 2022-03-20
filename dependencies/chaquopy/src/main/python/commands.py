import sqlite3
import os.path
import json
import threading

TABLE_SHOPPINGLISTS = "shoppinglists"
TABLE_SHOPITEMS = "shopitems"

package_dir = os.path.abspath(os.path.dirname(__file__))
db_dir = os.path.join(package_dir, 'shopping_lists.db')


class Commands:

    lock = threading.Lock()

    connection = sqlite3.connect(db_dir, check_same_thread = False)
    connection.row_factory = sqlite3.Row
    cursor = connection.cursor()

    def __init__(self):
        create_shoppinglists_table = f"""
        CREATE TABLE IF NOT EXISTS
        {TABLE_SHOPPINGLISTS}(id INTEGER PRIMARY KEY, name TEXT NOT NULL)
        """
        self.cursor.execute(create_shoppinglists_table)

        create_shopitems_table = f"""
            CREATE TABLE IF NOT EXISTS
            {TABLE_SHOPITEMS} (
            id INTEGER PRIMARY KEY,
            list_id INTEGER NOT NULL,
            name TEXT NOT NULL,
            quantity INTEGER NOT NULL,
            checked INTEGER NOT NULL,
            FOREIGN KEY(list_id) REFERENCES {TABLE_SHOPPINGLISTS}(id)
            )
        """
        self.cursor.execute(create_shopitems_table)

    def dict_from_row(self):
        return [dict(row) for row in self.cursor.fetchall()]

    def get_shoppinglists(self):
        self.cursor.execute(
            f"SELECT * FROM {TABLE_SHOPPINGLISTS}"
        )

        shopping_lists = self.dict_from_row()
        for shopping_list in shopping_lists:
            self.cursor.execute(
                f"SELECT * FROM {TABLE_SHOPITEMS} WHERE list_id = {shopping_list['id']} ORDER BY checked ASC"
            )
            shop_items = self.dict_from_row()
            shopping_list['items'] = shop_items

        return json.dumps(shopping_lists)

    def get_shoppinglist_by_id(self, list_id):
        self.cursor.execute(
            f"SELECT * FROM {TABLE_SHOPPINGLISTS} WHERE id = {list_id}"
        )
        shopping_list = dict(self.cursor.fetchone())

        self.cursor.execute(
            f"SELECT * FROM {TABLE_SHOPITEMS} WHERE list_id = {shopping_list['id']} ORDER BY checked ASC"
        )
        shop_items = self.dict_from_row()
        shopping_list['items'] = shop_items

        return json.dumps(shopping_list)

    def get_shopitems(self, list_id):
        self.cursor.execute(
            f"SELECT * FROM {TABLE_SHOPITEMS} WHERE list_id = {list_id} ORDER BY checked ASC"
        )
        return json.dumps(self.dict_from_row())

    def insert_shoppinglist(self, name):
        try:
            self.lock.acquire(True)
            self.cursor.execute(
                f"INSERT INTO {TABLE_SHOPPINGLISTS} VALUES (NULL, '{name}')"
            )
            self.cursor.execute(
                f"SELECT * FROM {TABLE_SHOPPINGLISTS} WHERE id = {self.cursor.lastrowid}"
            )
            self.connection.commit()

            shopping_list = dict(self.cursor.fetchone())
            shopping_list['items'] = []

            res = json.dumps(shopping_list)
        finally:
            self.lock.release()
        return res

    def insert_shopitem(self, list_id, name, quantity, checked):
        try:
            self.lock.acquire(True)
            self.cursor.execute(
                f"INSERT INTO {TABLE_SHOPITEMS} VALUES (NULL, {list_id}, '{name}', {quantity}, {checked})"
            )
            self.cursor.execute(
                f"SELECT * FROM {TABLE_SHOPITEMS} WHERE id = {self.cursor.lastrowid}"
            )
            self.connection.commit()
            res = json.dumps(dict(self.cursor.fetchone()))
        finally:
            self.lock.release()
        return res

    def update_shoppinglist(self, list_id, name):
        try:
            self.lock.acquire(True)
            self.cursor.execute(
                f"UPDATE {TABLE_SHOPPINGLISTS} SET name = '{name}' WHERE id = {list_id}"
            )
            self.connection.commit()
            res = self.get_shoppinglist_by_id(list_id)
        finally:
            self.lock.release()
        return res

    def update_shopitem(self, current_name, new_name, quantity, checked):
        try:
            self.lock.acquire(True)
            self.cursor.execute(
                f"""
            UPDATE {TABLE_SHOPITEMS}
            SET name = '{new_name}', quantity = {quantity}, checked = {checked}
            WHERE name = {current_name}
            """
            )
            self.cursor.execute(
                f"SELECT * FROM {TABLE_SHOPITEMS} WHERE id = {item_id}"
            )
            self.connection.commit()
            res = json.dumps(dict(self.cursor.fetchone()))
        finally:
            self.lock.release()
        return res

    def delete_shoppinglist(self, list_id):
        try:
            self.lock.acquire(True)
            self.cursor.execute(
                f"DELETE FROM {TABLE_SHOPPINGLISTS} WHERE id = {list_id}"
            )
            self.cursor.execute(
                f"DELETE FROM {TABLE_SHOPITEMS} WHERE list_id = {list_id}"
            )
            self.connection.commit()
        finally:
            self.lock.release()

    def delete_shopitem(self, name):
        try:
            self.lock.acquire(True)
            self.cursor.execute(
                f"DELETE FROM {TABLE_SHOPITEMS} WHERE name = {name}"
            )
            self.connection.commit()
        finally:
            self.lock.release()

    def clear_all(self):
        try:
            self.lock.acquire(True)
            self.cursor.execute(f"DROP TABLE {TABLE_SHOPPINGLISTS}")
            self.cursor.execute(f"DROP TABLE {TABLE_SHOPITEMS}")
            self.connection.commit()
        finally:
            self.lock.release()

    def cleanup(self):
        self.cursor.close()
        self.connection.close()

