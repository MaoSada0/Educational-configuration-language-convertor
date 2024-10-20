# Tool that converts yaml into an educational configuration language

To use this
```
java jar <path to jar file> <path to yaml file> <path to output file>
```

## Example

- input file

``` yaml
const_user: &user qq

const_arr: &arr
  - first
  - s
  - t: fsdf

server:
  port:
    8080
  host: localhost
database:
  name: test_db
  #test
  const_pass: &pass fsdf
  credentials:
    user: *user
    username: root
    password: secret
    #test1
    smth: *arr
    also:
      - 1
      - 2
      - 3
      - gdfg
    another:
      - one:
          te:
            fsd:
              fds
            das:
              - sd
              - d
          ewt: fsd
      - two: 2
      - three: 3
```

- output file

``` txt
=test
=test1
(def user qq);
(def arr [first, s, {t=fsdf}]);
(def pass fsdf);
server
{
  port: 8080;
  host: localhost;
}
database
{
  name: test_db;
  credentials
  {
    user: |user|;
    username: root;
    password: secret;
    smth: |arr|;
    also [
      1: 1;
      2: 2;
      3: 3;
      gdfg: gdfg;
    ]
    another [
      one
      {
        te
        {
          fsd: fds;
          das[
            sd: sd;
            d: d;
          ]
        }
        ewt: fsd;
      }
      two: 2;
      three: 3;
    ]
  }
}

```
