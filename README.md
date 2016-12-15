# intercom-clj

A Clojure library for [Intercom's REST API](https://developers.intercom.com/reference)

## Usage

1. Put the following dependency in your `project.clj`

```clj
[intercom-clj "0.1.0"]
```

2. Set the  [Personal Access Token](https://developers.intercom.com/reference#section-using-personal-access-tokens) via:

```clj
(require '[intercom-clj.core :refer [set-access-token!]])
(set-access-token! "MY-ACCESS-TOKEN")
```
Or the `INTERCOM_ACCESS_TOKEN` environment variable.

3. Start making API calls:
```clj
(require '[intercom-clj.users :as users])
(users/show {:user_id 123123123})
(users/show {:email "my@email.com"})
(users/create {:email "my@email.com" :user_id 123 :name "John Doe"})
```

You get the point :)

## Help is Welcome

This is the seed of the library which I hope will grow over time and eventually be adpoted as the official Intercom Clojure client.
Pull requests for extending the current set of API calls are more than welcome!

## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
