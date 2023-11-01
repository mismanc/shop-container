# Shop Container

This container includes projects that

### [Shop Backend](https://github.com/mismanc/shop-back)

### [Shop Frontend](https://github.com/mismanc/shop-front)

In this application you will see a list of some items. You can simply select and buy them.
After clicking "Buy" button, an email including selected items will be sent to the email defined in the env variables.

## Available Scripts

In the project directory, you can run:

### `docker-compse up --build`

Open [http://localhost](http://localhost) to view it in your browser.

## Available Parameters

In the `docker-compose.yml` file you can set 

#### `SHOP_MAIL_RECEIVER`

environment variable for `web-shop` container to send email after order creation.
