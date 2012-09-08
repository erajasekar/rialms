/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 7/19/12
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */

data {
    features = [
            [
                    name: 'adaptive',
                    description: "Question can be scored adaptively over a sequence of attempts. This allows the candidate to alter their answer following feedback or to be posed additional questions based on their current answer"
            ]
            ,
            [
                    name: 'choice',
                    description: "Presents candidate with a set of choices accepts single choice as response"
            ]
            ,
            [
                    name: 'choice multiple',
                    description: "Presents candidate with a set of choices accepts multiple choices as response"
            ]
            ,
            [
                    name: 'order',
                    description: "Presents candidates with a number of choices and allows them to reorder them in correct order"
            ]
            ,
            [
                    name: 'associate',
                    description: "Presents candidates with a number of choices and allows them to create associations between them"
            ]
            ,
            [
                    name: 'match',
                    description: "Presents candidates with two sets of choices and allows them to create associations between pairs of choices in the two sets"
            ]
            ,
            [
                    name: 'gap match',
                    description: "Presents number gaps that the candidate can fill from an associated set of choices"
            ]
            ,
            [
                    name: 'inline choice',
                    description: "Presents set of choice as dropdown and allows candidate to choose one"
            ]
            ,
            [
                    name: 'text entry',
                    description: "Obtains a simple piece of text response from the candidate"
            ]
            ,
            [
                    name: 'hottext',
                    description: "Presents a passage of text with some hot words/phrases highlighted and selectable by the candidate"
            ]
            ,
            [
                    name: 'partial scoring',
                    description: "Candidate can be awarded partial scores based on number of correct answers"
            ]
            ,
            [
                    name: 'templated',
                    description: 'Same question can be repeated with different choices (and the correct answer) based on template'
            ]
            ,
            [
                    name: 'hint',
                    description: 'Presents an option of requesting hint'
            ]
            ,
            [
                    name: 'solution',
                    description: 'Presents an option of requesting solution'
            ]
            ,
            [
                    name: 'feedback',
                    description: "Presents feedback conditionally based on candidate's response"
            ]
            ,
            [
                    name: 'math',
                    description: "Illustrates the inclusion of a mathematical expression marked up with MathML"
            ]
            ,
            [
                    name: 'linear',
                    description: "Restricts the candidate to attempt each question in turn"
            ]
            ,
            [
                    name: 'nonlinear',
                    description: "Candidate is free to navigate to any item in the test at any time"
            ]
            ,
            [
                    name: 'individual',
                    description: "Candidate's responses are submitted item-by-item basis"
            ]
            ,
            [
                    name: 'simultaneous',
                    description: "Candidate's responses are all submitted together at the end of the testPart"
            ]
            ,
            [
                    name: 'timeout',
                    description: "Limits the amount of time the candidate is allowed for the test"
            ]
            ,
            [
                    name: 'disabled review',
                    description: "Candidate cannot go back and review previous questions in individual submission mode && for non-adaptive questions"
            ]
            ,
            [
                    name: 'branch',
                    description: "Number and order of questions presented can be changed depending on the candidate's responses to items presented earlier in the test"
            ]
            ,
            [
                    name: 'disabled skipping',
                    description: "Candidate cannot skip questions in test."
            ]
            ,
            [
                    name: 'max attempts',
                    description: "Limits maximum number of attempts allowed to respond to a question for non-adaptive questions"
            ]
            ,
            [
                    name: 'nested',
                    description: "Test can have sections nested within another section"
            ]
            ,
            [
                    name: 'multiple parts',
                    description: "Test can have multiple test parts"
            ]
            ,
            [
                    name: 'weight item outcomes',
                    description: "Overall test score can be computed based on add weight individual item score"
            ]
            ,
            [
                    name: 'score using item categories',
                    description: "Items can be categorized to one or more categories and score based on categories "
            ]
            ,
            [
                    name: 'early termination',
                    description: "Tests can be terminated early based on accumulated item outcomes"
            ]
            ,
            [
                    name: 'randomize order',
                    description: "Test can randomize order of items and sections"
            ]

    ]
}