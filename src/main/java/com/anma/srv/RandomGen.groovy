package com.anma.srv

class RandomGen {
    public static String getRandomString(length) {
        Random random = new Random()
        def lorem = 'Lorem ipsum dolor sit amet consectetur adipiscing elit maecenas interdum pellentesque, ' +
                'sollicitudin hendrerit cubilia primis nisl feugiat placerat tellus mauris'
        def loremArr = lorem.split(' ')
        def randomString = ''
        for (i in 0..<length) {
            randomString = randomString.concat(loremArr[random.nextInt(loremArr.length - 1)]).concat(' ')
        }
        return randomString
    }
}
